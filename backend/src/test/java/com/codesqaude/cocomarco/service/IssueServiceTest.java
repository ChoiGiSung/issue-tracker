package com.codesqaude.cocomarco.service;

import com.codesqaude.cocomarco.domain.issue.IssueRepository;
import com.codesqaude.cocomarco.domain.issue.model.Issue;
import com.codesqaude.cocomarco.domain.issue.model.IssueStatus;
import com.codesqaude.cocomarco.domain.issue.model.dto.IssueRequest;
import com.codesqaude.cocomarco.domain.label.Label;
import com.codesqaude.cocomarco.domain.label.LabelRepository;
import com.codesqaude.cocomarco.domain.milestone.MilestoneRepository;
import com.codesqaude.cocomarco.domain.user.User;
import com.codesqaude.cocomarco.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IssueServiceTest {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    MilestoneRepository milestoneRepository;


    IssueService issueService;

    UUID uuid;

    @BeforeEach
    void init() {
        issueService = new IssueService(issueRepository, userRepository, labelRepository, milestoneRepository, null);
        User save = userRepository.save(User.localUser("로컬아이디", "로컬비번", 0));
        uuid = save.getId();
    }

    @Test
    void createIssue() {
        String title = "타이틀";
        String text = "내용";
        IssueRequest issueRequest = null;

        Label label = labelRepository.save(new Label("라벨", "10x", "내용"));
        issueRequest = new IssueRequest(title, text, Arrays.asList(), Arrays.asList(label.getId()), null);
        Long issueId = issueService.create(issueRequest, uuid);
        Issue issue = issueRepository.findById(issueId).get();

        assertThat(issue.getMilestone()).isEqualTo(null);
        assertThat(issue.getStatus()).isEqualTo(IssueStatus.OPEN);
        assertThat(issue.getIssueLabels().size()).isEqualTo(1);
    }

}
