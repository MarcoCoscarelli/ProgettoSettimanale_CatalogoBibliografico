package Catalogo.Dao;

import Catalogo.Entity.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UtenteDao {
    private final EntityManager em;

    public UtenteDao(EntityManager em) {
        this.em = em;
    }

    public void save(Utente utente) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(utente);
        et.commit();
    }

    public Utente getById(int id) {
        return em.find(Utente.class, id);
    }

    public void delete(Utente utente) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.remove(em.contains(utente) ? utente : em.merge(utente)); // Assicurati di rimuovere l'entità correttamente
        et.commit();
    }
}
