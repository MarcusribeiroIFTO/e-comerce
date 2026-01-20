package pweb2.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "O campo não pode estar vazio")
    @Email(message = "O e-mail deve ser um e-mail válido")
    private String email;

    @NotBlank(message = "O telefone não pode ser vazio")
    private String telefone;

    @NotBlank(message = "A senha é obrigatoria!")
    private String senha;

    @NotNull(message = "A Role é obrigatoria")
    private String role;

    @Column(name = "reset_senha")
    private String resetSenha;

    @Column(name = "token_creation_date")
    private java.time.LocalDateTime tokenCreationDate;

    @OneToMany(mappedBy = "cliente")
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResetSenha() {
        return resetSenha;
    }

    public void setResetSenha(String resetSenha) {
        this.resetSenha = resetSenha;
    }

    public java.time.LocalDateTime getTokenCreationDate() {
        return tokenCreationDate;
    }

    public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
        this.tokenCreationDate = tokenCreationDate;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
    public String getIdentificacao(){
        if (this instanceof PessoaFisica){
            return ((PessoaFisica)this).getNome();
        } else if (this instanceof PessoaJuridica) {
            return ((PessoaJuridica)this).getRazaoSocial();
        }
        return "";
    }
    public abstract String getNome();
}
