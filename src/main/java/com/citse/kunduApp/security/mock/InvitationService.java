package com.citse.kunduApp.security.mock;

import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.utils.models.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository repository;
    private final UserDao userRepo;

    public Invitation save(String email,int userId){
        var user = userRepo.findById(userId).orElse(null);
        if(user==null)
            throw new KunduException(Services.INVITATION_SERVICE.name(), "User not found", HttpStatus.NOT_FOUND);
        var invitation_reserved = repository.findByEmail(email);
        if(invitation_reserved.isPresent())
            throw new KunduException(Services.INVITATION_SERVICE.name(), "Email already exists", HttpStatus.UNPROCESSABLE_ENTITY);
        var invitation = Invitation.builder()
                .email(email)
                .userReserve(user)
                .date(LocalDate.now())
                .build();
        return repository.save(invitation);
    }

    public void delete(int id){
        repository.deleteById(id);
    }
}
