
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE TABLE keys (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    private_key TEXT NOT NULL,
    public_key_s TEXT NOT NULL,
    public_key_r TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE hash_seeds(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seed Long NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
)

CREATE INDEX idx_users_name ON users(first_name, last_name);