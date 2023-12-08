package com.example.kursinisfx.hibernate;

import com.example.kursinisfx.model.Cargo;
import com.example.kursinisfx.model.Checkpoint;
import com.example.kursinisfx.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CommentHib {
    EntityManager entityManager = null;
    EntityManagerFactory entityManagerFactory = null;

    public CommentHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    //  CREATE ----------------------------------
    public void createComment(Comment comment) {
        entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(comment);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    //  DELETE ----------------------------------

    public void removeComment( int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Comment comment = null;
            try {
                comment = entityManager.getReference(Comment.class, id);
                comment.getId();
            } catch (Exception e) {
                System.out.println("There is no comment by given id");
            }
            entityManager.remove(comment);
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

    public Comment getCommentById(int id) {
        entityManager = entityManagerFactory.createEntityManager();
        Comment comment = null;
        try {
            entityManager.getTransaction().begin();
            comment = entityManager.find(Comment.class, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No such comment by given Id");
        }
        return comment;
    }


}
