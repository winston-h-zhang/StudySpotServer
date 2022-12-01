/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

@WebServlet(name = "Server", value = "/form")
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String JDBC_SERVER = "jdbc:mysql://34.171.110.43/csci201_studyspotfinalproject";
    private static String JDBC_LOCAL = "jdbc:mysql://localhost/csci201_studyspotfinalproject";
    private static String JDBC = JDBC_SERVER;
	private static String USER = "root";
	private static String PASSWORD = "151515";
	
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
        Gson gson = new Gson();
		out.write(gson.toJson(true));
		out.flush();
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		System.out.println(">> doPost");
		String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JsonObject jsonObject = null;
		jsonObject = JsonParser.parseString(json).getAsJsonObject();

		System.out.println(json);
		String type = jsonObject.get("type").getAsString();
		String data = jsonObject.get("data").toString();
		String res = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		switch (type) {
		case "allStudySpots": 
			res = getAllStudySpots(data); 
			break;
		case "studySpot": 
			res = getStudySpot(data); 
			break;
		case "reviews": 
			res = getReview(data); 
			break;
		case "tags": 
			res = getTags(data); 
			break;
		case "avgReview": 
			res = getAvgReview(data); 
			break;
		case "sendReview": 
			res = sendReview(data); 
			break;
		case "validate": 
			res = loginUser(data); 
			break;
		default: 
			res = "null";
			break;
		}

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		System.out.println("res:");
		System.out.println(res);
		out.write(res);
		out.flush();
	}
	
	public String getAllStudySpots(String data) {
		Gson gson = new Gson();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<StudySpotData> spots = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM study_spots");
			while (rs.next()) {
				StudySpotData ss = new StudySpotData(
					rs.getString("name"),  
					rs.getString("location"), 
					rs.getDouble("latitude"), 
					rs.getDouble("longitude"),
					rs.getString("hours"), 
					rs.getBoolean("busy"), 
					rs.getBoolean("quiet"), 
					rs.getBoolean("outlets"));
				spots.add(ss);
			}
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return gson.toJson(spots);
	}

	public String getStudySpot(String data) {
		Gson gson = new Gson();
		StudySpotName ssn = gson.fromJson(data, StudySpotName.class);
		String name = ssn.name;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		StudySpotData ss = null;
		try {
			conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM study_spots WHERE name=\"" + name + "\"");
			if (!rs.next()) {
				return gson.toJson(null);
			}
			ss = new StudySpotData(
				rs.getString("name"),  
				rs.getString("location"), 
				rs.getDouble("latitude"), 
				rs.getDouble("longitude"),
				rs.getString("hours"), 
				rs.getBoolean("busy"), 
				rs.getBoolean("quiet"), 
				rs.getBoolean("outlets"));
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return gson.toJson(ss);
	}

	public String getReview(String data) {
		Gson gson = new Gson();
		StudySpotName ssn = gson.fromJson(data, StudySpotName.class);
		String name = ssn.name;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String> reviews = new ArrayList<String>();
		try {
			conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM reviews WHERE study_spot=\"" + name + "\"");
			while (rs.next()) {
				reviews.add(rs.getString("review"));
			}
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		// TODO: what if null?
		return gson.toJson(reviews);
	}

	public String getTags(String data) {
		Gson gson = new Gson();
		Tags tags = gson.fromJson(data, Tags.class);
		String name = tags.name;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean failed = false;
		try {
			conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
			st = conn.createStatement();
			String stmt = String.format(
					"UPDATE study_spot \r\n"
					+ "SET busy = %b, quiet = %b, outlets = %b\r\n"
					+ "WHERE name=%s;", tags.busy, tags.quiet, tags.outlets, name);
			rs = st.executeQuery(stmt);
		} catch (SQLException sqle) {
			failed = true;
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return gson.toJson(!failed);
	}

	public String getAvgReview(String data) {
		Gson gson = new Gson();
		StudySpotName ssn = gson.fromJson(data, StudySpotName.class);
		String name = ssn.name;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Double avg = null;
		try {
			conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
			st = conn.createStatement();
			String stmt = String.format("SELECT * FROM study_spots WHERE name=\"%s\"", name);
			rs = st.executeQuery(stmt);
			rs.next();
			double sum = rs.getDouble("sumReviews");
			int num = rs.getInt("numReviews");
			System.out.println(">> getAvgReview: " + sum + " " + num);
			if (num == 0) return "null";
			avg = sum / num;
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return gson.toJson(avg);
	}

	public String sendReview(String data) {
		Gson gson = new Gson();
		SendReview review = gson.fromJson(data, SendReview.class);
		String name = review.name;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean failed = false;
		try {
			conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
			st = conn.createStatement();
			String stmt = String.format(
					"INSERT INTO reviews\r\n"
					+ "VALUES (%s, %s);\r\n"
					, review.review, name);
			rs = st.executeQuery(stmt);
		} catch (SQLException sqle) {
			failed = true;
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return gson.toJson(!failed);
	}
	
	public String loginUser(String data) {
		Gson gson = new Gson();
		LoginData login = gson.fromJson(data, LoginData.class);
		String email = login.email;
		String password = login.password;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
        	conn = DriverManager.getConnection(JDBC, USER, PASSWORD);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * from users WHERE username=\"" + email + "\" AND password=\"" + password + "\"");
            if (rs.next()) {
                if (rs.getString("username") == null) {
                    return "false";
                } else {
                    return "true";
                }
            }
        } catch (SQLException sqle) {
            System.out.println("SQLException in login");
            sqle.printStackTrace();
            return "false";
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }

        return "false";
    }
}

class StudySpotName {
	String name;
}

class Tags {
	String name;
	boolean busy;
	boolean quiet;
	boolean outlets;
}

class SendReview {
	String name;
	String review;
}

class RegisterData {
	
}

class LoginData {
	String email;
	String password;
}

class StudySpotData {
	String name;
    String location;
    double latitude;
    double longitude;
    String hours;
    boolean busy;
    boolean quiet;
    boolean outlets;

    public StudySpotData(String name, String location, double latitude, double longitude, String hours,
			boolean busy, boolean quiet, boolean outlets) {
		super();
		this.name = name;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hours = hours;
		this.busy = busy;
		this.quiet = quiet;
		this.outlets = outlets;
	}
}

class StudySpotsData {
    ArrayList<StudySpotData> data;
    
    public StudySpotsData() {
    	this.data = new ArrayList<>();
    }
}