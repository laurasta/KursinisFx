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
    EntityManager entityManager = null;
    EntityManagerFactory entityManagerFactory = null;

    public DestinationHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    //  CREATE ----------------------------------

    public void createDestination(Destination destination) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(destination);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
    public void createCheckpoint(Checkpoint checkpoint) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(checkpoint);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
    public void createCargo(Cargo cargo) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(cargo);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    //  UPDATE ----------------------------------
    public void updateDestination(Destination destination) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(destination);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
    public void updateCheckpoint(Checkpoint checkpoint) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(checkpoint);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    //  DELETE ----------------------------------

    public void removeCargo( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Cargo cargo = null;
            try {
                cargo = entityManager.getReference(Cargo.class, id);
                cargo.getId();
            } catch (Exception e) {
                System.out.println("There is no cargo by given id");
            }
            entityManager.remove(cargo);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
    public void removeCheckpoint( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Checkpoint checkpoint = null;
            try {
                checkpoint = entityManager.getReference(Checkpoint.class, id);
                checkpoint.getId();
            } catch (Exception e) {
                System.out.println("There is no checkpoint by given id");
            }
            entityManager.remove(checkpoint);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
    public void removeDestination( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Destination destination = null;
            try {
                destination = entityManager.getReference(Destination.class, id);
                destination.getId();
            } catch (Exception e) {
                System.out.println("There is no destination by given id");
            }
            entityManager.remove(destination);
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
    public Cargo getCargoById(int id) {
        entityManager = entityManagerFactory.createEntityManager();
        Cargo cargo = null;
        try {
            entityManager.getTransaction().begin();
            cargo = entityManager.find(Cargo.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such cargo by given Id");
        }
        return cargo;
    }
    public Checkpoint getCheckpointById(int id) {
        entityManager = entityManagerFactory.createEntityManager();
        Checkpoint checkpoint = null;
        try {
            entityManager.getTransaction().begin();
            checkpoint = entityManager.find(Checkpoint.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such checkpoint by given Id");
        }
        return checkpoint;
    }
    public Destination getDestinationById(int id) {
        entityManager = entityManagerFactory.createEntityManager();
        Destination destination = null;
        try {
            entityManager.getTransaction().begin();
            destination = entityManager.find(Destination.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such destination by given Id");
        }
        return destination;
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
            return null;
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
            return null;
        }
    }

    public List<Vehicle> getAllVehicle(){
        entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Vehicle.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return new ArrayList<>();
    }

    public List<Destination> getAllDestinations() {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Destination.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return new ArrayList<>();
    }
}
