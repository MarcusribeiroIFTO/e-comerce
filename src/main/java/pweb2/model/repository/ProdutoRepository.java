package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb2.model.entity.Produto;

import java.util.List;

import static org.apache.el.parser.ELParserConstants.CONCAT;
import static org.springframework.data.repository.query.parser.Part.Type.LIKE;

@Repository
@Transactional
public class ProdutoRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Produto produto) {
        if (produto.getId() == null) {
            em.persist(produto);
        } else {
            em.merge(produto);
        }
    }

    public Produto findById(Long id) {
        return em.find(Produto.class, id);
    }

    public List<Produto> findAll(int page, int size) {
        return em.createQuery("select p from Produto p", Produto.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public void excluir(Long id) {
        Produto produto = findById(id);
        if (produto != null) {
            em.remove(produto);
        }

    }
    public Long countAll(){
        return em.createQuery("select count (p) from Produto p", Long.class).getSingleResult();
    }

    public List<Produto> findByDescricaoContainingIgnoreCase(String descricao, Long departamentoId,int page, int size){
                                                             StringBuilder jpql = new StringBuilder("SELECT produto FROM Produto produto where 1= 1");
        if (descricao != null && !descricao.isEmpty()){
            jpql.append(" and LOWER(produto.descricao) like LOWER (CONCAT('%', :descricao, '%'))");
        }
        if (departamentoId != null) {
            jpql.append(" and produto.departamento.id = :departamentoId");
        }
        Query query = em.createQuery(jpql.toString(), Produto.class);
        if (descricao != null && !descricao.isEmpty()){
            query.setParameter("descricao", descricao);
        }
        if (departamentoId != null) {
            query.setParameter("departamentoid", departamentoId);
        }
        return query.setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
    }

    public Long countByDescricaoContainingIgnoreCase(String descricao, Long departamentoid) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(produto) FROM Produto produto where 1= 1");
        if (descricao != null && !descricao.isEmpty()){
            jpql.append("AND LOWER(produto.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))");
        }
        if (departamentoid != null) {
            jpql.append(" and produto.departamento.id = :departamentoid");
        }
        Query query = em.createQuery(jpql.toString(), Long.class);
        if (descricao != null && !descricao.isEmpty()){
            query.setParameter("descricao", descricao);
        }
        if (departamentoid != null) {
            query.setParameter("departamentoid", departamentoid);
        }
        return (Long) query.getSingleResult();
    }
    public Long countByDepartamentoID(Long departamentoId) {
        return em.createQuery("SELECT COUNT (produto) from Produto produto where  produto.departamento.id = :departamentoid", Long.class)
                .setParameter("departamentoid", departamentoId)
                .getSingleResult();
    }
    public List<Produto> findByDepartamentoId(Long departamentoId, int page, int size) {
        return em.createQuery("SELECT p FROM Produto p WHERE p.departamento.id = :departamentoId", Produto.class)
                .setParameter("departamentoId", departamentoId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }
}
