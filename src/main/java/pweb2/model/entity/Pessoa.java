package pweb2.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String telefone;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venda> vendas= new ArrayList<>();

    public Pessoa() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public List<Venda> getVendas() {
        return vendas;
    }
    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
    public void addVenda(Venda venda) {
        vendas.add(venda);
        venda.setCliente(this);
    }

    public void removeVenda(Venda venda) {
        vendas.remove(venda);
        venda.setCliente(null);
    }

    @Transient
    public String getNomeExibicao() {
        if (this instanceof PessoaFisica) {
            return ((PessoaFisica) this).getNome();
        } else if (this instanceof PessoaJuridica) {
            return ((PessoaJuridica) this).getRazaoSocial();
        }
        return "Cliente";
    }
}
