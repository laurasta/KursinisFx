package com.example.kursinisfx.hibernate;

import com.example.kursinisfx.model.Cargo;
import com.example.kursinisfx.model.User;
import com.example.kursinisfx.model.Vehicle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class VehicleHib {
    EntityManager entityManager = null;
    EntityManagerFactory entityManagerFactory = null;

    public VehicleHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    //  CREATE ----------------------------------
    public void createVehicle(Vehicle vehicle) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(vehicle);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    //  DELETE ----------------------------------
    public void removeVehicle( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Vehicle vehicle = null;
            try {
                vehicle = entityManager.getReference(Vehicle.class, id);
                vehicle.getId();
            } catch (Exception e) {
                System.out.println("There is no vehicle by given id");
            }
            entityManager.remove(vehicle);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    //  READ ----------------------------------
    public Vehicle getVehicleById(int id) {
        entityManager = entityManagerFactory.createEntityManager();
        Vehicle vehicle = null;
        try {
            entityManager.getTransaction().begin();
            vehicle = entityManager.find(Vehicle.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such vehicle by given Id");
        }
        return vehicle;
    }

    public List<Vehicle> getAllVehicles() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Vehicle.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return null;
    }
}
