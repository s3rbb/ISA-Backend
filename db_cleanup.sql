-- Optional one-time cleanup for the taskmanager database (MySQL / phpMyAdmin).
--
-- Spring's ddl-auto=update ADDS new tables/columns automatically but never REMOVES
-- old ones. The Task entity used to have "description" and "assignee_id" columns that
-- no longer exist in the code, so this script drops those leftovers.
--
-- NOTE: the "comments" and "refresh_tokens" tables ARE used again, so we do NOT drop them.
-- Back up first if you have data you care about.

USE taskmanager;

-- Drop the old assignee_id foreign key (its name is auto-generated, so we look it up first)...
SET @fk := (SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME   = 'tasks'
              AND COLUMN_NAME  = 'assignee_id'
              AND REFERENCED_TABLE_NAME IS NOT NULL
            LIMIT 1);
SET @sql := IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE tasks DROP FOREIGN KEY ', @fk));
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- ...then drop the unused columns themselves.
ALTER TABLE tasks DROP COLUMN IF EXISTS assignee_id;
ALTER TABLE tasks DROP COLUMN IF EXISTS description;
