package br.com.softplan.person.web.rest;

import br.com.softplan.person.service.GitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * REST controller for managing {@link}.
 */
@RestController
@RequestMapping("/source")
public class GitResource {

    private final Logger log = LoggerFactory.getLogger(GitResource.class);

    @Autowired
    private GitService gitService;

    /**
     * {@code GET  /source : link.
     */
    @GetMapping("")
    public void getLink(HttpServletResponse response) throws IOException {
        log.debug("REST request to get City : {}");
        response.sendRedirect(gitService.getLink());
    }

}
