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
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

public class UserHib {
    private final EntityManagerFactory entityManagerFactory;
    private static final Logger logger = Logger.getLogger(UserHib.class.getName());


    public UserHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private static class CloseableEntityManager implements AutoCloseable {
        private final EntityManager entityManager;

        public CloseableEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        public EntityManager getEntityManager() {
            return entityManager;
        }

        @Override
        public void close() {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    private void performTransaction(Consumer<EntityManager> transactionLogic) {
        try (CloseableEntityManager closeableEntityManager = new CloseableEntityManager(entityManagerFactory.createEntityManager())) {
            EntityManager entityManager = closeableEntityManager.getEntityManager();
            entityManager.getTransaction().begin();
            transactionLogic.accept(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> T performTransactionWithResult(Function<EntityManager, T> transactionLogic) {
        try (CloseableEntityManager closeableEntityManager = new CloseableEntityManager(entityManagerFactory.createEntityManager())) {
            EntityManager entityManager = closeableEntityManager.getEntityManager();
            entityManager.getTransaction().begin();
            T result = transactionLogic.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createUser(User user) {
        performTransaction(entityManager -> entityManager.persist(user));
    }

    public void editUser(User user) {
        performTransaction(entityManager -> entityManager.merge(user));
    }

    public void removeUser(int id) {
        performTransaction(entityManager -> {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
            } else {
                logger.warning("There is no user with the given id: " + id);
            }
        });
    }

    public Trucker getTruckerById(int id) {
        return performTransactionWithResult(entityManager -> entityManager.find(Trucker.class, id));
    }

    public Manager getManagerById(int id) {
        return performTransactionWithResult(entityManager -> entityManager.find(Manager.class, id));
    }

    public User getUserById(int id) {
        return performTransactionWithResult(entityManager -> entityManager.find(User.class, id));
    }

    public User getUserByLoginData(String login, String psw) {
        return performTransactionWithResult(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), psw)));
            try {
                Query q = entityManager.createQuery(query);
                return (User) q.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    public List<User> getAllUsers() {
        return performTransactionWithResult(entityManager -> {
            CriteriaQuery<User> query = entityManager.getCriteriaBuilder().createQuery(User.class);
            query.select(query.from(User.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        });
    }

    public List<Trucker> getAllTruckers() {
        return performTransactionWithResult(entityManager -> {
            CriteriaQuery<Trucker> query = entityManager.getCriteriaBuilder().createQuery(Trucker.class);
            query.select(query.from(Trucker.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        });
    }

    public List<Manager> getAllManagers() {
        return performTransactionWithResult(entityManager -> {
            CriteriaQuery<Manager> query = entityManager.getCriteriaBuilder().createQuery(Manager.class);
            query.select(query.from(Manager.class));
            Query q = entityManager.createQuery(query);
            return q.getResultList();
        });
    }
}
