package com.example.kursinisfx;

import com.example.kursinisfx.hibernate.UserHib;
import com.example.kursinisfx.model.DriverLicenses;
import com.example.kursinisfx.model.Manager;
import com.example.kursinisfx.model.Trucker;
import com.example.kursinisfx.model.User;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestHibernate {
    public static void main (String[] args){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TruckSystem");
        UserHib userHib = new UserHib(entityManagerFactory);
        Manager manager = new Manager("ad", "ad", "ad","ad","ad","ad","ad", true);
        userHib.createUser(manager);
    }
}
