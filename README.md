# ProjektSauer_backend
Stream2gether

BRANCHING HOW-TO
    START NEW FEATURE:
        Is there a new feature to be added the dev has to branch a new branch from master into 'feature/SN-[increasing_number]' (feature/ is the subfolder)
	FEATURE INCLUDING FRONTEND & BACKEND CHANGES:
		If a feature includes changes in both the front and the backend the dev has to branch front and backend master branches accroding to to START NEW FEATURE (see above). Notify the reviewer via ticket that one has to checkout front and backend feature-branches to review the feature. Don't be afraid to communicate with the dev and vice versa.
    WORKING ON THE FEATURE:
        Each commit while implementing the feature should be committed with a commit message containing the prefix: SN-[increasing_number]: e.g. 'SN-001:'
    FEATURE IS DONE AND READY FOR REVIEW:
        If the feature is ready to be reviewed the dev drags/moves the ticket to the review column (trello). Now another dev can review the ticket.
    AFTER REVIEW:
        If the review is done and there are no further changes needed. The reviewer merges the feature branch into the master branch.
        Of Course, if the reviewer needs help merging, the developer should always support the reviewer at merging.