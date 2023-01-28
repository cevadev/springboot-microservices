#!/usr/bin/env bash
# En ocasiones necesitamos levantar todos los proyectos y hacer una prueba global para comprobar el
# funcionamiento de app.
# HOST=localhost PORT=7000 ./test-ms-all.bash
#

# host y port con valores por defecto
:${HOST=localhost}
# PUERTO PARA TRABAJAR CON DOCKER
:${HOST=8080}
#:${PORT=7000}

:${PROD_ID_REVS_RECS=1}
:${PROD_ID_NOT_FOUND=13}
:${PROD_ID_NO_RECS=113}
:${PROD_ID_NO_REVS=213}

# funcion que verifica si es correcta la llamada
funcion assertCurl(){
  # esperamos el status http code 200 0 204
  local expectedHttpCode = $1
  # pasamos el codigo http
  local curlCmd = "$2 -w \"%{http_code}\""
  local result = $(eval $curlCmd)
  # de la variable result obtenemos el httpCode
  local httpCode = "${result:(-3)}"

  RESPONSE = '' && (( ${#result} > 3 )) && RESPONSE = "${result%???}"

  # validamos que el codigo http sea igual al codigo esperado
  if [ "$httpCode" = "$expectedHttpCode"]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
    echo " Test FAILED, EXPECTED HTTP CODE: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
    # que comando fallo
    echo "- Failing command: $curlCmd"
    echo "- Response body: $RESPONSE"
    exit 1
  fi
}

funcion assertEqual(){
  local expected = $1
  local actual = $2
  if [ "$actual" = "$expected" ]
    then
      echo "Test OK (actual value: $actual)"
    else
      echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WHIT DIFFERENCES"
      exit 1
  fi
}

# probamos un url
function testUrl() {
    url = $@
    if $url -ks -f -o /dev/null
    then
      return 0
    else
      return 1
    fi;
}

function waitForService() {
    # capturamos el archivo de la derecha
    url = $@
    echo -n "Wait for: $url..."
    n=0
    until testUrl $url
    do
      # n inicia en 0
      n = $((n + 1))
      if [[ $n == 100 ]]
      then
        echo "Give up"
        exit 1
      else
        # ponemos un delay para que se levanten los 4 springboots
        sleep 3
        echo -n ", retry #$n"
      fi
    done

    echo "DONE, continues..."
}

# establecemos el entorno
set -e

echo "Starting tests..." `date`

# mostramos el valor elegido para host y port, o mostramos el valor por defecto
echo "HOST=${HOST}"
echo "PORT=${PORT}"

# validamos si le pasamos al archivo bash un argumento start. si es asi iniciamos los microservicios
if [[ $@ == *"start"* ]]]
then
  # nos aseguramos en terminar cualquier contenedor de docker orphans
  echo "Restarting the test environment..."
  echo "docker-compose down --remove-orphans"
  docker-compose down --remove-orphans
  # levantamos todo
  echo "docker-compose up -d"
  docker-compose up -d
fi

## al momento de correr el comando docker-compose up -d habra unos segundos de espera hasta que logre inicar los microservicios
## hacemos una espera hasta que el curl responda con la funcion waitForService
wairForService curl http://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS


# Verificamos un request normal, que espera las 3 recomendaciones y revisiones
assertCurl 200 "curl http://$HOST:$PORT/product-composite/$PROD_ID_REVS_RECS -s"
# verificamos si el $PROD_ID_REVS_RECS es igual al productId que viene en el response
assertEqual $PROD_ID_REVS_RECS $(echo $RESPONSE | jq .productId)
# si la llamade es OK entonces debemos tener 3 reviews y 3 recommendations
assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")

# verificamos que un 404 (not found) error se retorne para un productId no existente
assertCurl 404 "curl https://$HOST:$PORT/product-composite/$PROD_ID_NOT_FOUND -s"
assertEqual "No product found for productId: $PROD_ID_NOT_FOUND" "$(echo $RESPONSE | jq -r .message)"

# verificamos que no se retornan recomendaciones para el productId $PROD_ID_NO_RECS
assertCurl 200 "curl http://$HOST:$PORT/product-composite/$PROD_ID_NO_RECS -s"
assertEqual $PROD_ID_NO_RECS $(echo $RESPONSE | jq .productId)
assertEqual 0 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")

# verificamos que no se retornan reviews para el productId $PROD_ID_NO_REVS
assertCurl 200 "curl http://$HOST:$PORT/product-composite/$PROD_ID_NO_REVS -s"
assertEqual $PROD_ID_NO_REVS $(echo $RESPONSE | jq .productId)
assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 0 $(echo $RESPONSE | jq ".reviews | length")

# verificamos que un 422(unprocessable entity) retorna un error para el productId
assertCurl 422 "curl http://$HOST:$PORT/product-composite/-1 s"
assertEqual "\"Invalid productId: -1\"" "$(echo $RESPONSE | jq .message)"

# verificamos que un 400 (bad request) retorna un error para un productId que no esta en el rangi
assertCurl 400 "curl http://$HOST:$PORT/product-composite/no-integer s"
assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | jq .message)"

# si se le pasa al archivo bash el argumento stop, detenemos el docker
if [[ $@ == *"stop"* ]]
then
  echo "We are done, stopping the test environment..."
  echo "$ docker-compose down"
  docker-compose down
fi

echo "End, all test OK: " `date`