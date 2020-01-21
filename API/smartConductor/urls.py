from django.urls import path
from smartConductor import views

urlpatterns = [
    path('verify/', views.verify_token),
]
