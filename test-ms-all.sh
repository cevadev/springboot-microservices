
# En ocasiones necesitamos levantar todos los proyectos y hacer una prueba global para comprobar el
# funcionamiento de app.
# HOST=localhost PORT=7000 ./test-ms-all.bash
#

# host y port con valores por defecto
:${HOST=localhost}
:${PORT=7000}

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

# establecemos el entorno
set -e
# mostramos el valor elegido para host y port, o mostramos el valor por defecto
echo "HOST=${HOST}"
echo "PORT=${PORT}"

# Verificamos un request normal, que espera las 3 recomendaciones y revisiones
assertCurl 200 "curl http://$HOST:$PORT/product-composite/$PROD_PROD_ID_REVS_RECS -s"

# verificamos si el $PROD_ID_REVS_RECS es igual al productId que viene en el response
assertEqual $PROD_ID_REVS_RECS $(echo $RESPONSE | jq .productId)

# si la llamade es OK entonces debemos tener 3 reviews y 3 recommendations
assertEqual 3 $(echo $RESPONSE | jq ".recommendations | length")
assertEqual 3 $(echo $RESPONSE | jq ".reviews | length")

echo "End, all test OK: " `date`
