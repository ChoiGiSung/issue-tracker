package com.codesqaude.cocomarco.service;

import com.codesqaude.cocomarco.common.exception.notfound.NotFoundIssueException;
import com.codesqaude.cocomarco.common.exception.notfound.NotFoundMilestoneException;
import com.codesqaude.cocomarco.common.exception.notfound.NotFoundUserException;
import com.codesqaude.cocomarco.domain.issue.IssueRepository;
import com.codesqaude.cocomarco.domain.issue.IssueSearchRepository;
import com.codesqaude.cocomarco.domain.issue.model.Assignment;
import com.codesqaude.cocomarco.domain.issue.model.Issue;
import com.codesqaude.cocomarco.domain.issue.model.IssueLabel;
import com.codesqaude.cocomarco.domain.issue.model.dto.*;
import com.codesqaude.cocomarco.domain.label.Label;
import com.codesqaude.cocomarco.domain.label.LabelRepository;
import com.codesqaude.cocomarco.domain.milestone.Milestone;
import com.codesqaude.cocomarco.domain.milestone.MilestoneRepository;
import com.codesqaude.cocomarco.domain.user.User;
import com.codesqaude.cocomarco.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssueService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final MilestoneRepository milestoneRepository;
    private final IssueSearchRepository issueSearchRepository;


    @Transactional
    public Long create(IssueRequest issueRequest, UUID writerId) {
        User writer = userRepository.findById(writerId).orElseThrow(NotFoundUserException::new);
        List<User> assignees = userRepository.findAllById(issueRequest.getUserIds());
        List<Label> labels = labelRepository.findAllById(issueRequest.getLabels());
        //todo null 처리
        Milestone milestone = null;
        if (issueRequest.getMilestone() != null) {
            milestone = milestoneRepository.findById(issueRequest.getMilestone()).orElseThrow(NotFoundMilestoneException::new);
        }
        List<IssueLabel> issueLabels = labels.stream().map(IssueLabel::createIssueLabel).collect(Collectors.toList());
        List<Assignment> assignments = assignees.stream().map(Assignment::createAssignment).collect(Collectors.toList());

        Issue issue = Issue.createIssue(writer, issueRequest.getTitle(), issueRequest.getText(), assignments, issueLabels, milestone);

        return issueRepository.save(issue).getId();
    }

    public IssueDetailResponse showDetail(Long issueId) {
        Issue issue = issueRepository.findByIdFetch(issueId).orElseThrow(NotFoundIssueException::new);
        return IssueDetailResponse.of(issue);
    }

    public IssueListResponseWrapper showList(IssueSearchRequest request) {
        List<Issue> issues = issueSearchRepository.searchByBuilder(request);
        List<IssueListResponse> responses = issues.stream().map(IssueListResponse::of).collect(Collectors.toList());
        return new IssueListResponseWrapper(responses);
    }

    public Issue findById(Long issueId) {
        return issueRepository.findById(issueId).orElseThrow(NotFoundIssueException::new);
    }
}
