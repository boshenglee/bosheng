"""empty message

Revision ID: f9a972b6a81e
Revises: 4c5b4b1f8a23
Create Date: 2022-04-28 12:51:37.107800

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'f9a972b6a81e'
down_revision = '4c5b4b1f8a23'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('job', sa.Column('assign_to', sa.Integer(), nullable=True))
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('job', 'asign_to')
    # ### end Alembic commands ###
