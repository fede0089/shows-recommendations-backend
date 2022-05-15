APP=shows-recomendations-backend

build-image: ## Build the image
	docker build --no-cache -t ${APP} .

run: ## Run image, binding port 8080 of the container to port 8080 of the host machine
	docker run -t -p=8080:8080 --name="${APP}" ${APP}

up: build-image run

stop: ## Stop and remove a running container
	docker stop ${APP}; docker rm ${APP}

