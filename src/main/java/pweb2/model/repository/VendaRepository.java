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

public class VendaRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
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
    @Transactional
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

    public List<Venda> buscarPorClientesEData(
            List<Pessoa> clientes,
            LocalDateTime dataInicial,
            LocalDateTime dataFinal) {

        if (clientes == null || clientes.isEmpty()) {
            return List.of();
        }

        return em.createQuery(
                        "SELECT v FROM Venda v " +
                                "WHERE v.cliente IN :clientes " +
                                "AND v.dataVenda BETWEEN :dataInicial AND :dataFinal " +
                                "ORDER BY v.dataVenda DESC",
                        Venda.class
                )
                .setParameter("clientes", clientes)
                .setParameter("dataInicial", dataInicial)
                .setParameter("dataFinal", dataFinal)
                .getResultList();
    }
    public List<Venda> buscarPorClienteEData(
            Pessoa cliente,
            LocalDateTime dataInicial,
            LocalDateTime dataFinal) {

        if (cliente == null) {
            return List.of();
        }

        return em.createQuery(
                        "SELECT v FROM Venda v " +
                                "WHERE v.cliente = :cliente " +
                                "AND v.dataVenda BETWEEN :dataInicial AND :dataFinal " +
                                "ORDER BY v.dataVenda DESC",
                        Venda.class
                )
                .setParameter("cliente", cliente)
                .setParameter("dataInicial", dataInicial)
                .setParameter("dataFinal", dataFinal)
                .getResultList();
    }


    public List<Venda> buscarPorClientes(List<Pessoa> clientes){
        if (clientes == null || clientes.isEmpty()){
            return List.of();
        }
        String jpql = "SELECT v from Venda v where v.cliente in :clientes order by v.dataVenda DESC";
        Query query = em.createQuery(jpql);
        query.setParameter("clientes", clientes);
        return query.getResultList();
    }

    public List<Venda> buscarPorCliente(Pessoa cliente) {
        Query query = em.createQuery(
                "select v from Venda v where v.cliente = :cliente order by v.dataVenda desc"
        );
        query.setParameter("cliente", cliente);
        return query.getResultList();
    }





}
