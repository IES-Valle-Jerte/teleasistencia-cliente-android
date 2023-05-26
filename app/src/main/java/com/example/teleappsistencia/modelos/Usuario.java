package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * Clase POJO "Usuario" utilizada para parsear la respuesta JSON del servidor.
 */
public class Usuario implements Serializable {

    private final static long serialVersionUID = 2592565805411682085L;

    /**
     * Atributos de la clase
     */

    @SerializedName("id")
    private int pk;
    @SerializedName("url")
    private String url;
    @SerializedName("is_active")
    private Boolean isActive;
    @SerializedName("database_id")
    private Integer database;
    @SerializedName("last_login")
    private String lastLogin;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("date_joined")
    private Date dateJoined;
    @SerializedName("groups")
    private Object groups;
    @SerializedName("imagen")
    private Imagen imagen;

    /**
     * Getters y setters
     */
    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() { return isActive; }

    public void setActive(Boolean active) { isActive = active; }

    public Integer getDatabase() { return database; }

    public void setDatabase(Integer database) { this.database = database; }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
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

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Object getGroups() {
        return groups;
    }

    public void setGroups(Object groups) {
        this.groups = groups;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return this.firstName+" "+this.lastName;
    }
}