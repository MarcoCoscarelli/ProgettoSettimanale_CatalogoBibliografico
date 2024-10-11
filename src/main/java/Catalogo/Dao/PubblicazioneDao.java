package Catalogo.Dao;

import Catalogo.Entity.Pubblicazione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "pubblicazioni")
public class PubblicazioneDao {

    private final EntityManager em;

    public PubblicazioneDao(EntityManager em) {
        this.em = em;
    }

    public void save(Pubblicazione pubblicazione) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(pubblicazione);
        et.commit();
    }

    public Pubblicazione getById(int id) {
        return em.find(Pubblicazione.class, id);
    }

    public void delete(Pubblicazione pubblicazione) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.remove(em.contains(pubblicazione) ? pubblicazione : em.merge(pubblicazione)); // Assicurati di rimuovere l'entità correttamente
        et.commit();
    }

    public int removeByIsbn(String Isbn) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        Query query = em.createQuery("DELETE FROM Pubblicazione p WHERE p.codiceISBN=:Isbn");
        query.setParameter("Isbn", Isbn);

        int recordEliminati = query.executeUpdate();
        et.commit();
        return recordEliminati;
    }

    public Pubblicazione searchByIsbn(String Isbn) {
        Query query = em.createQuery("SELECT p FROM Pubblicazione p WHERE p.codiceISBN=:Isbn");
        query.setParameter("Isbn", Isbn);
        try {
            return (Pubblicazione) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Pubblicazione> searchByYearPublishing(int anno) {
        Query query = em.createQuery("SELECT p FROM Pubblicazione p WHERE p.annoPubblicazione=:anno");
        query.setParameter("anno", anno);
        return query.getResultList();
    }

    public List<Pubblicazione> searchByAuthor(String autore) {
        Query query = em.createQuery("SELECT p FROM Pubblicazione p WHERE p.autore=:autore");
        query.setParameter("autore", autore);
        return query.getResultList();
    }

    public List<Pubblicazione> searchByPartTitle(String titolo) {
        Query query = em.createQuery("SELECT p FROM Pubblicazione p WHERE p.titolo LIKE :titolo");
        query.setParameter("titolo", "%" + titolo + "%");
        return query.getResultList();
    }
}
