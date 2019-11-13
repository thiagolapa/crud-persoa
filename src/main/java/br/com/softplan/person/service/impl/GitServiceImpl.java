package br.com.softplan.person.service.impl;

import br.com.softplan.person.service.GitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GitServiceImpl implements GitService {

    private final Logger log = LoggerFactory.getLogger(GitServiceImpl.class);

    private static final String LINK_GIT = "https://github.com/thiagolapa/rafa";

    /**
     * Get one git link.
     * @return the link.
     */
    @Override
    public String getLink() {
        log.debug("Request to get link Git : {}");
        return LINK_GIT;
    }

}
