ALTER TABLE request
  CHANGE label title VARCHAR(2000);

ALTER TABLE request
  ADD COLUMN owner VARCHAR(250);

ALTER TABLE request
  ADD COLUMN repo VARCHAR(250);

ALTER TABLE request
  ADD COLUMN issue_number VARCHAR(100);