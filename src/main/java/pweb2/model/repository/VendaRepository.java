package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb2.model.entity.Pessoa;
import pweb2.model.entity.Venda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class VendaRepository {
    @PersistenceContext
    private EntityManager em;

    public void salvar(Venda venda) {
        if (venda.getId() == null){
            em.persist(venda);
        }else {
            em.merge(venda);
        }
    }
    public Integer buscarUltimoNumeroVenda() {
        return em.createQuery(
                "select max(v.numeroVenda) from Venda v", Integer.class
        ).getSingleResult();
    }
    public Venda findById(Long id) {
        return em.find(Venda.class, id);
    }

    public List<Venda> findAll() {

        Query query = em.createQuery("from Venda ");
        return query.getResultList();
    }
    public void excluirVEnda(Long id) {
        em.createQuery("DELETE FROM Venda ").executeUpdate();
    }
    public void excluir(Long id) {
        Venda venda = em.find(Venda.class, id);
        if (venda != null) {
            em.remove(venda);
        }
    }
    public void editar(Venda venda) {
        em.merge(venda);
    }
    public List<Venda> buscarPorData(LocalDateTime inicio, LocalDateTime fim) {

        String jpql = "SELECT v FROM Venda v WHERE 1=1";

        if (inicio != null && fim != null) {
            jpql += " AND v.data BETWEEN :inicio AND :fim";
        }

        jpql += " ORDER BY v.data DESC";

        TypedQuery<Venda> query = em.createQuery(jpql, Venda.class);

        if (inicio != null && fim != null) {
            query.setParameter("inicio", inicio);
            query.setParameter("fim", fim);
        }

        return query.getResultList();
    }

    public List<Venda> buscarPorClienteEData(List<Pessoa> clientes, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if (clientes == null || clientes.isEmpty()) return List.of();

        Query query = em.createQuery(
                "SELECT v from Venda v where v.cliente in :clientes and  " +
                        "v.dataVenda between  :dataInicial " +
                        "and :dataFinal order by v.dataVenda desc" );
        query.setParameter("clientes", clientes);
        query.setParameter("dataInicial", dataInicial);
        query.setParameter("dataFinal", dataFinal);
        return query.getResultList();
    }

    public List<Venda> buscarPorCliente(Pessoa cliente) {
        Query query = em.createQuery("select v from Venda v where v.cliente = :cliente order by v.dataVenda desc ");
        query.setParameter("cliente", cliente);
        return query.getResultList();
    }



}
