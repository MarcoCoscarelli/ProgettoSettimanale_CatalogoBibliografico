package Catalogo.Dao;

import Catalogo.Entity.Prestito;
import Catalogo.Entity.Pubblicazione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.List;

public class PrestitoDao {

    private final EntityManager em;

    public PrestitoDao(EntityManager em) {
        this.em = em;
    }

    public void save(Prestito prestito) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(prestito);
        et.commit();
    }

    public Prestito getById(int id) {
        return em.find(Prestito.class, id);
    }

    public void delete(Prestito prestito) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.remove(em.contains(prestito) ? prestito : em.merge(prestito)); // Assicurati di rimuovere l'entità correttamente
        et.commit();
    }

    public List<Pubblicazione> getElementiInPrestito(int tessera) {
        Query query = em.createQuery("SELECT pub FROM Utente u " +
                "JOIN u.prestiti pr " +  // Assumendo che ci sia una relazione in Utente per i prestiti
                "JOIN pr.elementoPrestato pub " +
                "WHERE u.numeroTessera = :tessera AND pr.dataRestituzioneEffettiva IS NULL");
        query.setParameter("tessera", tessera);
        return query.getResultList();
    }

    public List<Prestito> getPrestitiScadutiNonRestituiti() {
        Query query = em.createQuery("SELECT pr FROM Prestito pr " +
                "WHERE pr.dataRestituzionePrevista < CURRENT_DATE " +
                "AND pr.dataRestituzioneEffettiva IS NULL");
        return query.getResultList();
    }
}

