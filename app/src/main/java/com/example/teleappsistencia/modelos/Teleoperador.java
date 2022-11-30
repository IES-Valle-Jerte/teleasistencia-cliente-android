package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Teleoperador implements Serializable {

    /**
     * Atributos de la clase
     */

    @SerializedName("id")
    private int id;
    @SerializedName("password")
    private String password;
    @SerializedName("last_login")
    private Date lastLogin;
    @SerializedName("is_superuser")
    private boolean is_superuser;
    @SerializedName("username")
    private String username;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("is_staff")
    private boolean is_staff;
    @SerializedName("is_active")
    private boolean is_active;
    @SerializedName("date_joined")
    private Date dateJoined;
    @SerializedName("groups")
    private List<Object> groups;
    @SerializedName("user_permissions")
    private List<Object> user_permissions = null;

    // Solo de prueba hasta que se implemente el login
    // FIXME: hay que poder acceder al id_teloperador de forma estática
    public static int id_teleoperador = 11;

    /**
     * Getters y setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isIs_superuser() {
        return is_superuser;
    }

    public void setIs_superuser(boolean is_superuser) {
        this.is_superuser = is_superuser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIs_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public List<Object> getGroups() {
        return groups;
    }

    public void setGroups(List<Object> groups) {
        this.groups = groups;
    }

    public List<Object> getUser_permissions() {
        return user_permissions;
    }

    public void setUser_permissions(List<Object> user_permissions) {
        this.user_permissions = user_permissions;
    }
}
