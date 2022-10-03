ALTER TABLE ulab_edu.person ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE';
comment on column ulab_edu.person.status is 'Статус пользовтеля'