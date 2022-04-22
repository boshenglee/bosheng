"""empty message

Revision ID: 5b042f0d7e54
Revises: e898f7f03220
Create Date: 2022-04-22 13:00:30.170772

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '5b042f0d7e54'
down_revision = 'e898f7f03220'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('movement', sa.Column('robot_id', sa.Integer(), nullable=True))
    op.create_foreign_key(None, 'movement', 'robot', ['robot_id'], ['id'])
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_constraint(None, 'movement', type_='foreignkey')
    op.drop_column('movement', 'robot_id')
    # ### end Alembic commands ###