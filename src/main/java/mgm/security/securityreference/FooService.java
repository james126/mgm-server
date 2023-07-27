package mgm.security.secdev;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class FooService {

    @PreAuthorize("isAuthenticated() and hasRole('admin')")
    public void doSomething(){

    }
}
