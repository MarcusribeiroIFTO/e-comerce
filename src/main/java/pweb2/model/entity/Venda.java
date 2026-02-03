package pweb2.model.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private Integer numeroVenda;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime data;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Pessoa cliente;

    public Venda() {}

    public List<ItemVenda> getItens() {
        return itens;
    }
    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Pessoa getCliente() {
        return cliente;
    }
    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }
    public Integer getNumeroVenda() {
        return numeroVenda;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime dataVenda) {
        this.data = dataVenda;
    }

    public Double total(){
        if (itens == null ) return 0.0;
        double soma = 0.0;
        for (ItemVenda i : itens) {
            soma += i.total().doubleValue();
        }
        return soma;
    }

    @Transient
    public Double getValor(){
        return total();
    }
    @Transient
    private ItemVenda itemTemp =  new ItemVenda();
    public ItemVenda getItemTemp() {
        return itemTemp;
    }
    public void setNumeroVenda(Integer numeroVenda) {
        this.numeroVenda = numeroVenda;
    }
}
