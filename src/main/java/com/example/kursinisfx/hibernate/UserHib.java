package com.example.kursinisfx.hibernate;

import com.example.kursinisfx.model.Manager;
import com.example.kursinisfx.model.Trucker;
import com.example.kursinisfx.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserHib {
    EntityManagerFactory entityManagerFactory = null;

    public UserHib(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    //  CREATE ----------------------------------
    public void createUser(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
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

    //  UPDATE ----------------------------------

    public void editUser(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
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
    public void removeUser( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            User user = null;
            try {
                user = entityManager.getReference(User.class, id);
                user.getId();
            } catch (Exception e) {
                System.out.println("There is no user by given id");
            }
            entityManager.remove(user);
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
    public Trucker getTruckerById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Trucker trucker = null;
        try {
            entityManager.getTransaction().begin();
            trucker = entityManager.find(Trucker.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such trucker by given Id");
        }
        return trucker;
    }
    public Manager getManagerById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Manager manager = null;
        try {
            entityManager.getTransaction().begin();
            manager = entityManager.find(Manager.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such manager by given Id");
        }
        return manager;
    }

    public User getUserById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = null;
        try {
            entityManager.getTransaction().begin();
            user = entityManager.getReference(User.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("There is no user by given id");
        }
        return user;
    }

    public User getUserByLoginData(String login, String psw) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query q = null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), psw)));
        try {
            q = entityManager.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<User> getAllUsers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(User.class));
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
    public List<Trucker> getAllTruckers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Trucker.class));
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

    public List<Manager> getAllManagers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Manager.class));
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
