package com.example.kursinisfx.hibernate;

import com.example.kursinisfx.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DestinationHib {

    private final EntityManagerFactory entityManagerFactory;

    public DestinationHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private void performTransaction(EntityManager entityManager, Runnable transactionLogic) {
        try {
            entityManager.getTransaction().begin();
            transactionLogic.run();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            entityManager.close();
        }
    }



    // CREATE ----------------------------------

    public void createDestination(Destination destination) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> entityManager.persist(destination));
    }

    public void createCheckpoint(Checkpoint checkpoint) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> entityManager.persist(checkpoint));
    }

    public void createCargo(Cargo cargo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> entityManager.persist(cargo));
    }

    // UPDATE ----------------------------------

    public void updateDestination(Destination destination) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> entityManager.merge(destination));
    }

    public void updateCheckpoint(Checkpoint checkpoint) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> entityManager.merge(checkpoint));
    }

    // DELETE ----------------------------------

    public void removeCargo(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> {
            Cargo cargo = entityManager.getReference(Cargo.class, id);
            entityManager.remove(cargo);
        });
    }

    public void removeCheckpoint(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> {
            Checkpoint checkpoint = entityManager.getReference(Checkpoint.class, id);
            entityManager.remove(checkpoint);
        });
    }

    public void removeDestination(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        performTransaction(entityManager, () -> {
            Destination destination = entityManager.getReference(Destination.class, id);
            entityManager.remove(destination);
        });
    }

    // READ ----------------------------------

    public Cargo getCargoById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Cargo.class, id);
    }

    public Checkpoint getCheckpointById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Checkpoint.class, id);
    }

    public Destination getDestinationById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(Destination.class, id);
    }

    public List<Checkpoint> getCheckpointsByDestination(Destination destination) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Checkpoint> query = builder.createQuery(Checkpoint.class);
            Root<Checkpoint> checkpointRoot = query.from(Checkpoint.class);
            query.where(builder.equal(checkpointRoot.get("checkpointDestination"), destination));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }


    public List<Cargo> getCargosByCheckpoint(Checkpoint checkpoint) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cargo> query = builder.createQuery(Cargo.class);
            Root<Cargo> checkpointRoot = query.from(Cargo.class);
            query.where(builder.equal(checkpointRoot.get("cargoCheckpoint"), checkpoint));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        } finally {
            entityManager.close();
        }
    }


    public List<Vehicle> getAllVehicle() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Vehicle> query = entityManager.getCriteriaBuilder().createQuery(Vehicle.class);
            query.select(query.from(Vehicle.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return new ArrayList<>();
    }


    public List<Destination> getAllDestinations() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Destination> query = entityManager.getCriteriaBuilder().createQuery(Destination.class);
            query.select(query.from(Destination.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return new ArrayList<>();
    }
}