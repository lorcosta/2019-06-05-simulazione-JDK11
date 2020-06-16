package it.polito.tdp.crimes.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;



public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getAnni(){
		String sql="SELECT DISTINCT(YEAR(reported_date)) as anno "
				+ "FROM events ORDER BY anno";
		List<Integer> anni=new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Integer anno=res.getInt("anno");
					anni.add(anno);
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	public List<Integer> getMesi(){
		String sql="SELECT DISTINCT(MONTH(reported_date)) as anno "
				+ "FROM events ORDER BY anno";
		List<Integer> anni=new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Integer anno=res.getInt("anno");
					anni.add(anno);
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	public List<Integer> getGiorni(){
		String sql="SELECT DISTINCT(DAY(reported_date)) as anno "
				+ "FROM events ORDER BY anno";
		List<Integer> anni=new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Integer anno=res.getInt("anno");
					anni.add(anno);
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getVertici(){
		String sql="SELECT DISTINCT(district_id) as district "
				+ "FROM events "
				+ "ORDER BY district";
		List<Integer> vertici=new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Integer vertice=res.getInt("district");
					vertici.add(vertice);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return vertici ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

	public Double getLatMedia(Integer anno, Integer vertice) {
		String sql="SELECT AVG(geo_lat) AS lat " + 
				"FROM events " + 
				"WHERE district_id=? AND YEAR(reported_date)=?";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, vertice);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.first()) {
				try {
					conn.close();
					return res.getDouble("lat");
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}else {
				conn.close();
				return null;
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		return null;
		
	}
	public Double getLongMedia(Integer anno, Integer vertice) {
		String sql="SELECT AVG(geo_lon) AS lon " + 
				"FROM events " + 
				"WHERE district_id=? AND YEAR(reported_date)=?";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, vertice);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.first()) {
				try {
					conn.close();
					return res.getDouble("lon");
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}else {
				conn.close();
				return null;
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		return null;
		
	}

	public Integer getDistrettoMin(Integer anno) {
		String sql="SELECT district_id, COUNT(*) as tot " + 
				"FROM events " + 
				"WHERE YEAR(reported_date)=? " + 
				"GROUP BY district_id " + 
				"ORDER BY tot " + 
				"LIMIT 1";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			ResultSet res = st.executeQuery() ;
			
			if(res.first()) {
				try {
					conn.close();
					return res.getInt("district_id");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}else {
				conn.close();
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		return null;
	}
	
	public List<Event> listAllEventsByDate(Integer anno,Integer mese,Integer giorno){
		String sql = "SELECT * FROM events WHERE YEAR(reported_date)=? AND MONTH(reported_date)=? AND DAY(reported_date)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}

}
