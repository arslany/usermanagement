CREATE TABLE
  USERS (
    id bigserial NOT NULL,
    username character varying(255) NOT NULL,
    firstname character varying(255) NOT NULL,
    surname character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    active boolean NOT NULL
  );

ALTER TABLE
  USERS
ADD
  CONSTRAINT user_pkey PRIMARY KEY (id);

INSERT INTO USERS (username, password, firstname, surname, active, email)
SELECT 'smitha', '1234', 'Alice', 'Smith', 'true','smitha@gmail.com'
WHERE NOT EXISTS (
SELECT 1 FROM USERS
WHERE username = 'alice'
);


