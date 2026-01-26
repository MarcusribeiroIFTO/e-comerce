package pweb2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pweb2.model.entity.Pessoa;
import pweb2.model.repository.PessoaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void generateResetPasswordToken(Pessoa pessoa){
        String token = UUID.randomUUID().toString();
        pessoa.setResetSenha(token);
        pessoa.setTokenCreationDate(LocalDateTime.now());
        pessoaRepository.update(pessoa);
    }

    public Pessoa findNyResetPasswordToken(String token){
        return pessoaRepository.findByResetSenhaToken(token);
    }

    public void updatePassword( Pessoa pessoa, String newPassword){
        pessoa.setSenha(passwordEncoder.encode(newPassword));
        pessoa.setResetSenha(null);
        pessoa.setTokenCreationDate(null);
        pessoaRepository.update(pessoa);
    }

}
