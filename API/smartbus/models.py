from django.db import models


class Stops(models.Model):
    id = models.CharField(max_length=20, primary_key=True)
    lat = models.CharField(max_length=10)
    lon = models.CharField(max_length=10)


class Bus(models.Model):
    number = models.CharField(max_length=8)
    src = models.CharField(max_length=20)
    dest = models.CharField(max_length=20)
    price = models.IntegerField()
    capacity = models.IntegerField()
    occupied = models.IntegerField()
    curr_lat = models.CharField(max_length=10)
    curr_lon = models.CharField(max_length=10)
    avg_speed = models.IntegerField()


class User(models.Model):
    id = models.CharField(max_length=50, primary_key=True)
    password = models.CharField(max_length=256)
    wallet_balance = models.IntegerField()


class Token(models.Model):
    id = models.CharField(max_length=6, primary_key=True)
    username = models.CharField(max_length=50)
    amount = models.IntegerField()