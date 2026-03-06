# Task D: Git and GitHub Repository, Versioning, and Workflow Report
**Module Outcome**: LO III  
**Project**: Ocean View Resort Management System  
**Date**: 2026-03-06

---

## 1. Task Objective
This report documents how version control was applied using Git and GitHub throughout development, including:
- Public repository setup and deployment
- Daily version updates with new features/modifications
- Version control techniques used
- Workflow demonstrations (branching, merging, tagging, release handling)

---

## 2. Public Repository and Report Links
Update this section with your actual links before submission.

- Public GitHub Repository: `https://github.com/<your-username>/<your-repo>`
- Project Report Link (inside repository docs): `https://github.com/<your-username>/<your-repo>/blob/main/docs/TASK_D_GIT_GITHUB_REPORT.md`
- Optional Release Page Link: `https://github.com/<your-username>/<your-repo>/releases`

---

## 3. Repository Initialization and Deployment

### 3.1 Local Repository Setup
```powershell
cd "D:\IntelliJ Projects\Ocean.View_Resort"
git init
git add .
git commit -m "chore: initial project import"
```

### 3.2 Create Public GitHub Repository and Connect Remote
```powershell
git branch -M main
git remote add origin https://github.com/<your-username>/<your-repo>.git
git push -u origin main
```

### 3.3 Daily Update Push Pattern
```powershell
git checkout -b feature/<short-feature-name>
git add .
git commit -m "feat: <what was added>"
git push -u origin feature/<short-feature-name>
```

After review/testing:
```powershell
git checkout main
git merge --no-ff feature/<short-feature-name> -m "merge: feature/<short-feature-name>"
git push origin main
```

---

## 4. Daily Version History (Evidence of Continuous Updates)
Use or adapt the table below with your real commit hashes and dates.

| Day | Date | Version | Branch | Main Changes | Commit/Tag Evidence |
|-----|------|---------|--------|--------------|---------------------|
| Day 1 | 2026-03-03 | v0.1.0 | `main` | Initial system structure, DB schema, base servlets/JSP | `git log --oneline` + tag `v0.1.0` |
| Day 2 | 2026-03-04 | v0.2.0 | `feature/admin-panel` | Admin management features and UI improvements | PR merge + tag `v0.2.0` |
| Day 3 | 2026-03-05 | v0.3.0 | `feature/testing-tdd` | Automated tests, TDD docs, test scripts | PR merge + tag `v0.3.0` |
| Day 4 | 2026-03-06 | v0.4.0 | `feature/auth-validation` | Login redirect/home validation improvements and documentation updates | PR merge + tag `v0.4.0` |

Create tags:
```powershell
git tag -a v0.1.0 -m "Initial project baseline"
git tag -a v0.2.0 -m "Admin module enhancements"
git tag -a v0.3.0 -m "Testing and TDD completion"
git tag -a v0.4.0 -m "Authentication and validation refinements"
git push origin --tags
```

---

## 5. Version Control Techniques Used

### 5.1 Branching Strategy
- `main`: stable and deployable branch
- `feature/*`: new features and enhancements
- `bugfix/*`: issue corrections
- `docs/*`: documentation-only changes
- `hotfix/*`: urgent production fixes

### 5.2 Commit Discipline
- Small, focused commits for traceability
- Clear commit messages (`feat`, `fix`, `docs`, `refactor`, `test`, `chore`)
- Frequent commits to reduce risk and simplify rollback

### 5.3 Merge and Integration
- Feature branches merged into `main` after testing
- `--no-ff` merges used to preserve feature history
- Conflict resolution documented in merge commits when required

### 5.4 Release Versioning
- Semantic-style tags (`v0.1.0`, `v0.2.0`, etc.)
- Each release tag maps to a working milestone
- Release notes summarize features/fixes and known limitations

### 5.5 Traceability and Auditability
- Change history visible via commit logs
- Milestone snapshots visible via tags/releases
- Branch names map directly to task scope

---

## 6. Workflow Demonstrations

### 6.1 New Feature Workflow
1. Create branch from `main`
2. Implement feature incrementally
3. Commit with descriptive messages
4. Push branch to GitHub
5. Open PR, review, merge
6. Tag release if milestone completed

### 6.2 Bug Fix Workflow
1. Create `bugfix/*` branch
2. Reproduce and fix defect
3. Add/update tests
4. Merge to `main` with clear fix commit trail

### 6.3 Documentation Workflow
1. Create `docs/*` branch
2. Update technical and submission docs
3. Commit docs changes separately from code changes
4. Merge and keep documentation versioned alongside source code

### 6.4 Hotfix Workflow
1. Branch from `main` as `hotfix/*`
2. Apply minimal urgent patch
3. Test and merge back immediately
4. Tag patched release (for example, `v0.4.1`)

---

## 7. Commands Used for Evidence
Run these commands and include screenshots/output in your submission appendix.

```powershell
git status
git log --oneline --graph --decorate --all
git branch -a
git tag -n
git remote -v
```

Optional detailed history:
```powershell
git log --pretty=format:"%h %ad %an %s" --date=short
```

---

## 8. Assessment Mapping (LO III)

| Requirement | Evidence in This Report |
|-------------|-------------------------|
| Public Git/GitHub repository created | Section 2 links + Section 3 deployment commands |
| Changes uploaded/deployed | Section 3 push workflow + commit history commands |
| Several versions over multiple days | Section 4 daily version history + tags |
| Version control techniques highlighted | Section 5 |
| Workflow demonstration | Section 6 |

---

## 9. Final Submission Checklist
- [ ] Repository is public and accessible without login
- [ ] README includes Task D report link
- [ ] Daily versions are visible in commits/tags
- [ ] At least 3-4 dated updates with meaningful modifications
- [ ] Workflow (feature/bugfix/docs/hotfix) is demonstrated
- [ ] This report link is shared in documentation and matches the live repository

---

## 10. Notes
- Keep commits chronological and meaningful.  
- Avoid one large final commit for the entire project.  
- Use tags to show clear milestone versions for grading visibility.

