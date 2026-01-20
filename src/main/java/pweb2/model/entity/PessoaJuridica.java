package pweb2.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_pessoa_juridica")
public class PessoaJuridica extends Pessoa{

    @Column(name = "razao_social")
    @NotBlank(message = "O campo Razão Social não pode estar vazio")
    private String razaoSocial;

    @NotBlank(message = "O CNPJ é obrigatorio")
    private String cnpj;

    public PessoaJuridica() {}

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    @Override
    public String getNome() {
        return "";
    }
    @Override
    public String getIdentificacao() {
        return razaoSocial;
    }
}
