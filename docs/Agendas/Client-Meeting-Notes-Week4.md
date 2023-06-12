# Notes meeting 2023-05-15

## Requirements: 

	- Must haves
		- 1 and 2 good
		- 3 not necessarily a must have
		- 4 good
		- 5 explain later in report that ansible will be used, can keep it generic in the requirements
		- 6 good (make graphs high level, intuitive for the user, more info when selecting a graph)
	- Should haves
		- 1 good, clarify (monitor software requires user accounts)
		- Extra requirement: Implement metadata based filtering in the dashboard
		- 2, 3, 4 good
		- 5 redundant, actual collection can't really be monitored, increase of storage can be monitored, however it is already in a different requirement
	- Could haves
		- 1 good, can use docker-ops
		- 2 low on priority list, only when everything finished
		- Extra could-have: Log system keeps track of when and what failed (list of errors, if its still not working or is it fixed, similar to gitlab issues)
	- Won't haves
		- Good
	
	- Non-functional
		- Documentation explains core idea, not step by step high-level (e.g. no need to explain ssh)
		
## Coding Requirements

	- The client prefers we only rely on ELK Stack, and has proposed that we document our progress using issues and merge
	requests on those issues.
	- The client believes that writing Ansible playbooks is sufficient for the project. Ansible playbooks would be used for
	the installation of the monitoring system on the nodes and perhaps the server - depending on the time.
	- However, the client did mention that if there is a hard requirement for us to code in order to pass the project, they
	are willing to reconsider some requirements which would allow us to write code.
	
### Further explanation
	
	- The possible solution to this issue that the group has considered, is splitting up parts of the monitoring such as CPU, Storage, RAM Usage, network status (checking if the node is connected to the network), into smaller parts of code and which
	would send requests to all HTTP endpoints from Elastic.

	- Considering the time constraints we believe that this is the best viable solution that we can rely on. If this is not sufficient we would like to get some feedback on what we should do in order to satisfy all the Software Project requirements.


## Rest of notes

	- Number of nodes in the network are around 30
	- Nodes have locations and codenames
	- Access to client's wiki possibly granted (client will create accounts to log into it)
	- No need to deal with failed-to-transfer data
	- Images on wiki
	- Incidents:
		- Windows update makes machines go down
		- Internet goes down, machines lose access
		- Animals chewing through fiber optic cables
		- Lost power
		- Weather elements damage intstruments
		- Expired passwords

## Meetings

	- Midterm presentation, client available wednesday all day, if not at all possible, then friday
	- Week 6 and 8, wednesday 10am-11am