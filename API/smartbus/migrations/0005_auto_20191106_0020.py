# Generated by Django 2.2.6 on 2019-11-05 18:50

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('smartbus', '0004_auto_20191106_0017'),
    ]

    operations = [
        migrations.AddField(
            model_name='bus',
            name='id',
            field=models.AutoField(auto_created=True, default=1, primary_key=True, serialize=False, verbose_name='ID'),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='bus',
            name='number',
            field=models.CharField(max_length=8),
        ),
    ]
