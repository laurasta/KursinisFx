package com.example.kursinisfx.hibernate;

import com.example.kursinisfx.model.Destination;
import com.example.kursinisfx.model.Forum;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class ForumHib {
    EntityManager entityManager = null;
    EntityManagerFactory entityManagerFactory = null;

    public ForumHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    //  CREATE ----------------------------------
    public void createForum(Forum forum) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(forum);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    //  DELETE ----------------------------------

    public void removeForum( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Forum forum = null;
            try {
                forum = entityManager.getReference(Forum.class, id);
                forum.getId();
            } catch (Exception e) {
                System.out.println("There is no forum by given id");
            }
            entityManager.remove(forum);
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
    public Forum getForumById(int id) {
        entityManager = entityManagerFactory.createEntityManager();
        Forum forum = null;
        try {
            entityManager.getTransaction().begin();
            forum = entityManager.find(Forum.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such forum by given Id");
        }
        return forum;
    }

    public List<Forum> getAllForums() {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaQuery<Object> query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(Forum.class));
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
