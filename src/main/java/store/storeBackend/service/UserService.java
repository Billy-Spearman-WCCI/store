package store.storeBackend.service;

import com.auth0.jwt.JWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.storeBackend.api.model.LoginBody;
import store.storeBackend.api.model.RegistrationBody;
import store.storeBackend.exception.UserAlreadyExistException;
import store.storeBackend.model.LocalUser;
import store.storeBackend.model.dao.LocalUserDAO;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;

    private JWTService jwtService;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser( RegistrationBody registrationBody) throws UserAlreadyExistException {
       if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                                                                                    localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent())
       {
           throw new UserAlreadyExistException();
        }
        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        return user= localUserDAO.save(user);



    }
    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if(opUser.isPresent()){
            LocalUser user = opUser.get();
            if(encryptionService.verifyPassword((loginBody.getPassword()),user.getPassword())){
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}

