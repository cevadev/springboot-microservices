#
# HOST=localhost PORT=7000 ./test-ms-all.bash
#

:${HOST=localhost}
:${PORT=7000}
:${PROD_ID_REVS_RECS=1}
:${PROD_ID_NOT_FOUND=13}
:${PROD_ID_NO_RECS=113}
:${PROD_ID_NO_REVS=213}

# funcion que verifica si es correcta la llamada
funcion assertCurl(){}

funcion assertEqual(){}