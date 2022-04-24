APP=shows-recomendations-backend

build-image: ## Build the container
	docker build --no-cache -t ${APP} .

run: ## Run container on port configured in `config.env`
	docker run -t -p=8080:8080 --name="${APP}" ${APP}

up: build-image run

stop: ## Stop and remove a running container
	docker stop ${APP}; docker rm ${APP}

