
## Task 2: Kubernetes Deployment

In this task, I tried extending the Task Manager application from Task 1 to run on **Kubernetes**. The main steps and implementations include:

1. **Dockerization**: Created **Dockerfiles** for the backend application and built Docker images.  
2. **Kubernetes Manifests**: Developed **YAML manifests** including a **Deployment** and **Service** to deploy the application on a Kubernetes cluster.  
3. **Local Cluster Setup**: Tested deployment on a single-node local Kubernetes cluster (**Minikube/Docker Desktop**).  
4. **MongoDB Deployment**: Deployed **MongoDB** in a separate pod using a **Helm chart**, with **persistent storage** to ensure data durability.  
5. **Environment Variables**: Configured the application to read **MongoDB connection details** from environment variables.  
6. **Exposing Endpoints**: Exposed application endpoints to the host machine via **NodePort/LoadBalancer**.  
7. **Dynamic Pod Execution**: Modified the **PUT /taskExecution** endpoint to programmatically create a new **Kubernetes pod** using the Kubernetes API and execute the provided shell command inside it (using a **BusyBox image**).  

Though i can't finish the task completely, I tried my best to meet the requirements

<img width="1366" height="768" alt="Screenshot 2025-10-20 134528" src="https://github.com/user-attachments/assets/07866d2f-2d36-4796-91e9-bb4effc174d9" />
<img width="1366" height="768" alt="Screenshot 2025-10-20 123352" src="https://github.com/user-attachments/assets/8d0242e5-5106-4dfc-a30e-09f55aef63d3" />

