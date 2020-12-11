package com.openapigateway.legacy0001;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LEGACY0001Controller {

    @Autowired
    private LEGACY0001Service legacy0001Service;

    @RequestMapping(value = "/LEGACY0001/", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAccounts(@RequestParam("interfaceId") String interfaceId, @RequestParam("id") String id) {
        log.debug("getAccounts {} {} {}", "start", interfaceId, id);

        User user = new User();
        user.setId(id);
        
        String accounts = legacy0001Service.getAccounts(interfaceId, user);

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/LEGACY0001/{userId}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAccountsByPk(@PathVariable String userId) {
        log.debug("getAccountsByPk {} {}", "start", userId);

        String accounts = legacy0001Service.getAccountsByPk(userId);

        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

}
