package pweb2.model.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Integer numeroVenda;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;
    private BigDecimal valor;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    private BigDecimal valorTotal = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Pessoa cliente;

    public Venda() {}
    public List<ItemVenda> getItens() {
        return itens;
    }
    public void addItem(ItemVenda item) {
        item.setVenda(this);
        itens.add(item);
        calcularTotal();
    }
    public void removeItem(ItemVenda item) {
        itens.remove(item);
        calcularTotal();
    }
    public void calcularTotal(){
        valorTotal = itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public Pessoa getCliente() {
        return cliente;
    }
    public void setCliente(Pessoa cliente) {
        this.cliente = cliente;
    }
    public BigDecimal getValorTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {
            BigDecimal subtotal = item.getValorUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);
        }

        return total;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getNumeroVenda() {
        return numeroVenda;
    }

    public void setNumeroVenda(Integer numeroVenda) {
        this.numeroVenda = numeroVenda;
    }
}
