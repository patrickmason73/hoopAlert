package learn.hoopAlert.domain;

import learn.hoopAlert.data.RoleRepository;
import learn.hoopAlert.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService{
    @Autowired
    private RoleRepository roleRepository;


    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }


    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
