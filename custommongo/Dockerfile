FROM mongo:8.0.1
COPY --chown=999 --chmod=600 keyFile /etc/mongo/keyFile
RUN chmod 400 /etc/mongo/keyFile
COPY --chown=999 --chmod=600 mongod.conf /etc/mongod.conf
