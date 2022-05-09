"""empty message

Revision ID: e5648909020e
Revises: 5de52fac7794
Create Date: 2022-05-05 10:18:10.204579

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy.dialects import postgresql

# revision identifiers, used by Alembic.
revision = 'e5648909020e'
down_revision = '5de52fac7794'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('charge', sa.Column('start_date', sa.DateTime(), nullable=True))
    op.drop_column('charge', 'start__date')
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('charge', sa.Column('start__date', postgresql.TIMESTAMP(), autoincrement=False, nullable=True))
    op.drop_column('charge', 'start_date')
    # ### end Alembic commands ###
