package com.codegnan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.codegnan.entity.User;

public class UserDaoImpl implements UserDao {
private String url;
private String username;
private String password;
	public UserDaoImpl(String url, String username, String password) {
	super();
	this.url = url;
	this.username = username;
	this.password = password;
}

	@Override
	public void addUser(User user) {
		try(Connection connection=DriverManager.getConnection(url, username, password)){
			String query="insert into users(name,email) values(?,?)";
			try(PreparedStatement statement=connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)){
				statement.setString(1,user.getName());
				statement.setString(2,user.getEmail());
				statement.executeUpdate();
				
				ResultSet resultSet=statement.getGeneratedKeys();
				if(resultSet.next()) {
					user.setId(resultSet.getInt(1));
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public User getUserById(int id) {
		User user=null;
		try(Connection connection=DriverManager.getConnection(url,username,password)){
			String query="select*from users where id=?";
			try(PreparedStatement statement=connection.prepareStatement(query)){
				statement.setInt(1, id);
				
				ResultSet resultSet=statement.executeQuery();
				if(resultSet.next()) {
					user=new User();
					user.setId(resultSet.getInt("id"));
					user.setName(resultSet.getString("name"));
					user.setEmail(resultSet.getString("email"));
					
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		List<User>users=new ArrayList<>();
		try(Connection connection=DriverManager.getConnection(url, username, password)){
			String sqlQuery="select*from users";
			try(Statement statement=connection.createStatement();
				ResultSet resultSet=statement.executeQuery(sqlQuery)){
				while(resultSet.next()) {
					User user=new User();
					user.setId(resultSet.getInt("id"));
					user.setName(resultSet.getString("name"));
					user.setEmail(resultSet.getString("email"));
					users.add(user);
				}
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public void updateUser(User user) {
		try(Connection connection=DriverManager.getConnection(url,username,password)){
			String query="update users set name=?,email=? where id=?";
			try(PreparedStatement statement=connection.prepareStatement(query)){
				statement.setString(1, user.getName());
				statement.setString(2, user.getEmail());
				statement.setInt(3, user.getId());
				statement.executeUpdate();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUser(int id) {
		try(Connection connection=DriverManager.getConnection(url,username,password)){
			String query="delete from users where id=?";
			try(PreparedStatement statement=connection.prepareStatement(query)){
				statement.setInt(1, id);
				statement.executeUpdate();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
